export function toggleIconRotate(collapseIcon: HTMLElement) {
  if (collapseIcon.classList.contains('svg-rotate')){
    collapseIcon.classList.remove('svg-rotate');
    collapseIcon.classList.add('svg-unrotate');
  }
  else {
    collapseIcon.classList.remove('svg-unrotate');
    collapseIcon.classList.add('svg-rotate');
  }
}