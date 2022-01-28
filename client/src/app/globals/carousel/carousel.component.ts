import { Component, ViewChild, OnInit, Input } from '@angular/core';
import { NgbCarousel, NgbSlideEvent, NgbSlideEventSource } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})

export class CarouselComponent implements OnInit {
  idImageToStyle = 0;
  addIndex = 0;

  @Input() images: [];
  @ViewChild('carousel', { static: true }) carousel: NgbCarousel;

  ngOnInit() {
    this.idImageToStyle = 0;
    this.carousel.pause();
  }

  selectImage(i) {
    this.idImageToStyle = i;
  }

  onSlide(slideEvent: NgbSlideEvent) {
    if (slideEvent.source === NgbSlideEventSource.ARROW_RIGHT) {
      this.idImageToStyle = this.idImageToStyle + 1;
      if (this.idImageToStyle === this.images.length) {
        this.idImageToStyle = 0;
      }
    } else {
      if (slideEvent.current === 'ngb-slide-0') {
        this.idImageToStyle = 0;
      }
      else if (this.idImageToStyle === 0) {
        this.idImageToStyle = this.images.length - 1;
      } else {
        this.idImageToStyle = this.idImageToStyle - 1;
      }
    }
  }
}
