import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SwiperComponent } from 'swiper/angular';

// import Swiper core and required modules
import SwiperCore, { Pagination, Scrollbar } from 'swiper/core';

// install Swiper modules
SwiperCore.use([Pagination, Scrollbar]);

@Component({
  selector: 'jhi-swiper',
  templateUrl: './swiperlib.component.html',
  styleUrls: ['./swiperlib.component.scss'],
})
export class SwiperlibComponent implements OnInit {
  @Input('data') data: any[] = [];
  @Output('onSelectEvent') onSelectEvent = new EventEmitter<MouseEvent>();

  constructor() {}

  ngOnInit(): void {}

  onSelect(event: MouseEvent): void {
    this.onSelectEvent.emit(event);
  }
}
