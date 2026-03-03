import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductoEditPage } from './producto-edit-page';

describe('ProductoEditPage', () => {
  let component: ProductoEditPage;
  let fixture: ComponentFixture<ProductoEditPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductoEditPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductoEditPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
