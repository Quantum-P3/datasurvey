import { Component, OnInit } from '@angular/core';
import { ICategoria } from '../../categoria/categoria.model';
import { Account } from '../../../core/auth/account.model';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { IEncuesta } from '../../encuesta/encuesta.model';
import { Subject } from 'rxjs';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaService } from '../../categoria/service/categoria.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { AccountService } from '../../../core/auth/account.service';
import { takeUntil } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { faPollH, faCalendarAlt, faStar, faListAlt, faFileAlt, faCreditCard } from '@fortawesome/free-solid-svg-icons';
import { IPlantilla } from '../../plantilla/plantilla.model';
import { PlantillaService } from '../../plantilla/service/plantilla.service';
import { EncuestaPublishDialogComponent } from '../../encuesta/encuesta-publish-dialog/encuesta-publish-dialog.component';
import { PaypalDialogComponent } from '../paypal-dialog/paypal-dialog.component';

@Component({
  selector: 'jhi-listar-tienda-plantilla',
  templateUrl: './listar-tienda-plantilla.component.html',
  styleUrls: ['./listar-tienda-plantilla.component.scss'],
})
export class ListarTiendaPlantillaComponent implements OnInit {
  public searchString: string;
  public searchCategoria: string;
  categorias?: ICategoria[];
  account: Account | null = null;
  public searchEncuestaPublica: string;
  notAccount: boolean = true;
  usuarioExtra: UsuarioExtra | null = null;
  plantillas?: IPlantilla[];
  successPayment = false;

  isLoading = false;
  private readonly destroy$ = new Subject<void>();

  faStar = faStar;
  faCalendarAlt = faCalendarAlt;
  faPollH = faPollH;
  faListAlt = faListAlt;
  faFileAlt = faFileAlt;
  faCreditCard = faCreditCard;

  constructor(
    protected plantillaService: PlantillaService,
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

    this.plantillaService.query().subscribe(
      (res: HttpResponse<IPlantilla[]>) => {
        this.isLoading = false;
        const plantillasBody = res.body ?? [];
        this.plantillas = plantillasBody.filter(e => e.estado === 'ACTIVE');
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

  trackId(index: number, item: IEncuesta): number {
    return item.id!;
  }

  triggerPaypalDialog(plantilla: IPlantilla): void {
    const modalRef = this.modalService.open(PaypalDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plantilla = plantilla;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'published') {
        this.successPayment = true;
        this.loadAll();
      }
    });
  }
}
