import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductoCreatePage } from './producto-create-page';

describe('ProductoCreatePage', () => {
  let component: ProductoCreatePage;
  let fixture: ComponentFixture<ProductoCreatePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductoCreatePage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductoCreatePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
