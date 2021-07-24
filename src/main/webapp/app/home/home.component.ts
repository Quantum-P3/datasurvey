import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { faShareAlt, faWindowMaximize, faPollH, faCalendarAlt, faAngleDown, faStar } from '@fortawesome/free-solid-svg-icons';

import * as $ from 'jquery';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();

  usuarioExtra: UsuarioExtra | null = null;
  encuestas?: IEncuesta[];
  isLoading = false;

  faStar = faStar;
  faCalendarAlt = faCalendarAlt;
  faWindowMaximize = faWindowMaximize;
  faPollH = faPollH;

  notAccount: boolean = true;

  constructor(
    protected encuestaService: EncuestaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService,
    protected router: Router
  ) {}

  ngOnInit(): void {
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
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngAfterViewInit(): void {}

  trackId(index: number, item: IEncuesta): number {
    return item.id!;
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

  openSurvey(event: any): void {
    const surveyId = event.target.getAttribute('data-id');
    this.router.navigate(['/encuesta', surveyId, 'edit']);
  }

  selectSurvey(event: any): void {
    document.querySelectorAll('.ds-list--entity').forEach(e => {
      e.classList.remove('active');
    });
    if (event.target.classList.contains('ds-list--entity')) {
      event.target.classList.add('active');
    }
  }

  counter(i: number) {
    return new Array(i);
  }
}
