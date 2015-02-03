var remoteAppServices = angular.module('remoteAppServices', [
    'ras.traducaoMensagens',
    'ngRoute'
]);

remoteAppServices.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/cadastro', {
        templateUrl: 'partials/cadastro.html'
      }).
      when('/home', {
        templateUrl: 'partials/home.html'
      }).
      when('/login', {
        templateUrl: 'partials/login.html'
      }).
      otherwise({
        redirectTo: '/home'
      });
  }]);

remoteAppServices.controller('RemoteAppServicesController', function ($scope) {
  $scope.nome = 'erico';
});