export interface RouteInfo {
  path: string;
  title: string;
  type: string;
  collapse?: string;
  icontype: string;
  // icon: string;
  children?: ChildrenItems[];
}

export interface ChildrenItems {
  path: string;
  title: string;
  ab: string;
  type?: string;
}

export const ADMIN_ROUTES: RouteInfo[] = [
  // {
  //   path: '/dashboard',
  //   title: 'Dashboard',
  //   type: 'link',
  //   icontype: 'nc-icon nc-chart-bar-32',
  // },

  { path: '/pagina-principal', title: 'Inicio', type: 'link', icontype: 'nc-icon nc-world-2' },
  {
    path: '/encuesta',
    title: 'Encuestas',
    type: 'link',
    icontype: 'nc-icon nc-paper',
  },
  {
    path: '/plantilla',
    title: 'Plantillas',
    type: 'link',
    icontype: 'nc-icon nc-album-2',
  },
  {
    path: '/categoria',
    title: 'Categorías',
    type: 'link',
    icontype: 'nc-icon nc-tag-content',
  },
  {
    path: '/usuario-extra',
    title: 'Usuarios',
    type: 'link',
    icontype: 'nc-icon nc-single-02',
  },
  {
    path: '/parametro-aplicacion/1/edit',
    title: 'Configuración',
    type: 'link',
    icontype: 'nc-icon nc-settings-gear-65',
  },
];

export const USER_ROUTES: RouteInfo[] = [
  { path: '/pagina-principal', title: 'Inicio', type: 'link', icontype: 'nc-icon nc-world-2' },
  {
    path: '/encuesta',
    title: 'Encuestas',
    type: 'link',
    icontype: 'nc-icon nc-paper',
  },
  // {
  //   path: '/tienda',
  //   title: 'Tienda',
  //   type: 'link',
  //   icontype: 'nc-icon nc-cart-simple',
  // },
  // {
  //   path: '/plantilla',
  //   title: 'Plantillas',
  //   type: 'link',
  //   icontype: 'nc-icon nc-album-2',
  // },
  // {
  //   path: '/colaboraciones',
  //   title: 'Colaboraciones',
  //   type: 'link',
  //   icontype: 'nc-icon nc-world-2',
  // },
];
