var remoteAppServices = angular.module('remoteAppServices', [
    'ras.traducaoMensagens',
    'ngRoute'
]);

remoteAppServices.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'partials/home.html'
      }).
      when('/login', {
        templateUrl: 'partials/login.html'
      }).
      when('/cadastro', {
        templateUrl: 'partials/protected/cadastro.html'
      }).
      when('/erro-de-autenticacao', {
        templateUrl: 'partials/erro-de-autenticacao.html'
      }).
      otherwise({
        redirectTo: '/home'
      });
  }]);

remoteAppServices.controller('RemoteAppServicesController', function ($scope) {
  $scope.nome = 'erico';
});