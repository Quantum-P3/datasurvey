import { Component, OnInit } from '@angular/core';
import { IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';
import { ParametroAplicacionService } from '../../parametro-aplicacion/service/parametro-aplicacion.service';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPlantilla } from '../../plantilla/plantilla.model';

@Component({
  selector: 'jhi-paypal-dialog',
  templateUrl: './paypal-dialog.component.html',
  styleUrls: ['./paypal-dialog.component.scss'],
})
export class PaypalDialogComponent implements OnInit {
  public payPalConfig?: IPayPalConfig;
  showSuccess = false;
  plantilla?: IPlantilla;

  constructor(protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    this.initConfig();
  }

  private initConfig(): void {
    debugger;
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'sb',
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
        debugger;
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then((details: any) => {
          //calls baxkend
          console.log('onApprove - you can get full order details inside onApprove: ', details);
        });
      },
      onClientAuthorization: data => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
        this.showSuccess = true;
      },
    };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
