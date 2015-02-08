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
      when('/erro/:mensagemId', {
        templateUrl: 'partials/erro.html',
        controller: 'ErroController'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);

remoteAppServices.controller('ErroController', function($scope, $routeParams) {
  $scope.mensagemId = $routeParams.mensagemId;
});

remoteAppServices.controller('RemoteAppServicesController', function ($scope) {
  $scope.nome = 'erico';
});