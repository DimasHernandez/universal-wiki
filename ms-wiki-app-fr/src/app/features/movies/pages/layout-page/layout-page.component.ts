import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from '@app/features/movies/interfaces';
import { AuthService } from '@core/services/auth.service';
import { SpinnerService } from '@shared/services/spinner.service';

@Component({
  selector: 'app-layout-page',
  standalone: false,
  templateUrl: './layout-page.component.html',
  styleUrl: './layout-page.component.css'
})
export class LayoutPageComponent implements OnInit {

  private readonly authService = inject(AuthService);
  private readonly spinnerService = inject(SpinnerService);
  loading = false;

  public sidebarMenuItems: MenuItem[] = [
    { label: 'Movies', icon: 'movie', url: '/movies/list' },
    { label: 'Series TV', icon: 'tv', url: '#' },
    { label: 'Anime', icon: 'video_library', url: '#' },
    { label: 'Comics', icon: 'auto_stories', url: '#' },
    { label: 'Books', icon: 'book', url: '#' },
  ]

  public logout(): void {
    this.authService.logout();
  }

  ngOnInit(): void {
    this.onChangeLoading();
  }

  onChangeLoading(): void {
    this.spinnerService.loading$.subscribe((loading) => {
      console.log('loading :>> ', loading);
      this.loading = loading;
    });
  }

}
