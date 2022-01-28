import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConexionModalComponent } from './conexion-modal.component';

describe('ConexionModalComponent', () => {
  let component: ConexionModalComponent;
  let fixture: ComponentFixture<ConexionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConexionModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConexionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
