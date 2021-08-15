import { Component, OnInit } from '@angular/core';
import { IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';
import { ParametroAplicacionService } from '../../parametro-aplicacion/service/parametro-aplicacion.service';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPlantilla } from '../../plantilla/plantilla.model';
import { faCreditCard, faCheck } from '@fortawesome/free-solid-svg-icons';
import { Account } from '../../../core/auth/account.model';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { IFactura } from '../../factura/factura.model';
import { takeUntil } from 'rxjs/operators';
import { AccountService } from '../../../core/auth/account.service';
import { Subject } from 'rxjs';
import * as dayjs from 'dayjs';
import { FacturaService } from '../../factura/service/factura.service';

@Component({
  selector: 'jhi-paypal-dialog',
  templateUrl: './paypal-dialog.component.html',
  styleUrls: ['./paypal-dialog.component.scss'],
})
export class PaypalDialogComponent implements OnInit {
  public payPalConfig?: IPayPalConfig;
  showSuccess = false;
  plantilla?: IPlantilla;
  account: Account | null = null;
  usuarioExtra: UsuarioExtra | null = null;
  factura?: IFactura;
  notAccount: boolean = true;
  private readonly destroy$ = new Subject<void>();

  faCreditCard = faCreditCard;
  faCheck = faCheck;

  constructor(
    protected facturaService: FacturaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activeModal: NgbActiveModal,
    protected accountService: AccountService
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

    this.getUser();

    this.initConfig();
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'AUIxW_mYvd_h3mMqTtHdrSNMJ9yPmJkpiOCkNq454vDxXCN6hgadgPHIX_9PTeQn1Qv8m-ozcQUQkUjZ',
      createOrderOnClient: data =>
        <ICreateOrderRequest>{
          intent: 'CAPTURE',
          purchase_units: [
            {
              amount: {
                currency_code: 'USD',
                value: String(this.plantilla?.precio),
                breakdown: {
                  item_total: {
                    currency_code: 'USD',
                    value: String(this.plantilla?.precio),
                  },
                },
              },
              items: [
                {
                  name: this.plantilla?.nombre,
                  quantity: '1',
                  unit_amount: {
                    currency_code: 'USD',
                    value: String(this.plantilla?.precio),
                  },
                },
              ],
            },
          ],
        },
      advanced: {
        commit: 'true',
      },
      style: {
        label: 'paypal',
        layout: 'vertical',
      },
      onApprove: (data, actions) => {
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then((details: any) => {
          //calls baxkend
          console.log('onApprove - you can get full order details inside onApprove: ', details);
        });
      },
      onClientAuthorization: data => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
        this.updateUser();
        this.sendReceipt();
      },
    };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  getUser(): void {
    // Get jhi_user and usuario_extra information
    if (this.account !== null) {
      this.usuarioExtraService.find(this.account.id).subscribe(usuarioExtra => {
        this.usuarioExtra = usuarioExtra.body;
      });
    }
  }

  updateUser(): void {
    this.usuarioExtra?.plantillas?.push(this.plantilla!);

    this.usuarioExtraService.update(this.usuarioExtra!).subscribe(() => {
      this.showSuccess = true;
    });
  }
  sendReceipt(): void {
    const now = dayjs();

    this.factura = {
      nombreUsuario: String(this.usuarioExtra?.id!),
      nombrePlantilla: this.plantilla?.nombre!,
      costo: this.plantilla?.precio,
      fecha: now,
    };

    this.facturaService.create(this.factura).subscribe(() => {
      this.showSuccess = true;
      this.cancel();
    });

    //send
  }

  freePurchase(): void {
    this.getUser();
    this.sendReceipt();
  }
}
