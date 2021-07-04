import { SwiperModule } from 'swiper/angular';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SwiperlibComponent } from './swiperlib/swiperlib.component';

@NgModule({
  declarations: [SwiperlibComponent],
  imports: [CommonModule, SwiperModule],
  exports: [SwiperModule, SwiperlibComponent],
})
export class ComponentsModule {}
