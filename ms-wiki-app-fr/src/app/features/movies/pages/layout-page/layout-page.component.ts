import { Component } from '@angular/core';
import { MenuItem } from '@app/features/movies/interfaces';

@Component({
  selector: 'app-layout-page',
  standalone: false,
  templateUrl: './layout-page.component.html',
  styleUrl: './layout-page.component.css'
})
export class LayoutPageComponent {

  public sidebarMenuItems: MenuItem[] = [
    { label: 'Movies', icon: 'movie', url: '/movies/list' },
    { label: 'Series TV', icon: 'tv', url: '#' },
    { label: 'Anime', icon: 'video_library', url: '#' },
    { label: 'Comics', icon: 'auto_stories', url: '#' },
    { label: 'Books', icon: 'book', url: '#' },
  ]

}
