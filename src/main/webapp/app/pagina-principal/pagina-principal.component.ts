import { Component, OnInit } from '@angular/core';
import { Account } from '../core/auth/account.model';
import { takeUntil } from 'rxjs/operators';
import { EncuestaService } from '../entities/encuesta/service/encuesta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaService } from '../entities/categoria/service/categoria.service';
import { UsuarioExtraService } from '../entities/usuario-extra/service/usuario-extra.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { AccountService } from '../core/auth/account.service';
import { HttpResponse } from '@angular/common/http';
import { IEncuesta } from '../entities/encuesta/encuesta.model';
import { UsuarioExtra } from '../entities/usuario-extra/usuario-extra.model';
import { Subject } from 'rxjs';

import { faPollH, faCalendarAlt, faStar, faListAlt, faFileAlt } from '@fortawesome/free-solid-svg-icons';
import { ICategoria } from '../entities/categoria/categoria.model';
import { AccesoEncuesta } from 'app/entities/enumerations/acceso-encuesta.model';
import { EncuestaPasswordDialogComponent } from 'app/entities/encuesta/encuesta-password-dialog/encuesta-password-dialog.component';

@Component({
  selector: 'jhi-pagina-principal',
  templateUrl: './pagina-principal.component.html',
  styleUrls: ['./pagina-principal.component.scss'],
})
export class PaginaPrincipalComponent implements OnInit {
  public searchString: string;
  public searchCategoria: string;
  categorias?: ICategoria[];
  account: Account | null = null;
  public searchEncuestaPublica: string;
  notAccount: boolean = true;
  usuarioExtra: UsuarioExtra | null = null;
  encuestas?: IEncuesta[];

  isLoading = false;
  private readonly destroy$ = new Subject<void>();

  faStar = faStar;
  faCalendarAlt = faCalendarAlt;
  faPollH = faPollH;
  faListAlt = faListAlt;
  faFileAlt = faFileAlt;

  constructor(
    protected encuestaService: EncuestaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService,
    protected router: Router
  ) {
    this.searchEncuestaPublica = '';
    this.searchString = '';
    this.searchCategoria = '';
  }

  ngOnInit(): void {
    this.searchEncuestaPublica = '';
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        if (account !== null) {
          this.account = account;
          this.notAccount = false;
        } else {
          this.notAccount = true;
        }
      });
    this.loadAll();
    this.loadAllCategorias();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadAll(): void {
    this.isLoading = true;

    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        const tmpEncuestas = res.body ?? [];
        this.encuestas = tmpEncuestas.filter(e => e.estado === 'ACTIVE' && e.acceso === 'PUBLIC');
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  loadAllCategorias(): void {
    this.isLoading = true;

    this.categoriaService.query().subscribe(
      (res: HttpResponse<ICategoria[]>) => {
        this.isLoading = false;
        this.categorias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  trackId(_index: number, item: IEncuesta): number {
    return item.id!;
  }

  completeEncuesta(encuesta: IEncuesta): void {
    this.router.navigate(['/encuesta', encuesta.id, 'complete']);
  }

  confirmPassword(encuesta: IEncuesta): void {
    const modalRef = this.modalService.open(EncuestaPasswordDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = encuesta;
    modalRef.closed.subscribe(isValid => {
      if (isValid) {
        // Load the survey
      }
    });
  }
}
