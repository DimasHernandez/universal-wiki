import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutPageComponent } from './pages/layout-page/layout-page.component';
import { ListPageComponent } from './pages/list-page/list-page.component';
import { MoviesRoutingModule } from './movies-routing.module';
import { MaterialModule } from '@app/material/material.module';


@NgModule({
  declarations: [
    LayoutPageComponent,
    ListPageComponent
  ],
  imports: [
    CommonModule,
    MoviesRoutingModule,
    MaterialModule,
  ]
})
export class MoviesModule { }
