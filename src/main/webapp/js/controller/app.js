var routerApp = angular.module('app', ['ngRoute']);
routerApp.controller('appController',
    function($scope, $rootScope) {
        // logo-name
        $scope.appName = "Sportif";
        // show-hide homepage nav-bar
        $rootScope.showNavBar = true;
    });
routerApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/', {
        templateUrl: 'views/main-template.html',
        controller: 'mainViewController'
    }).
    when('/login', {
        templateUrl: 'views/login-template.html',
        controller: 'loginViewController'
    }).
    when('/signup', {
        templateUrl: 'views/signup-template.html',
        controller: 'signupViewController'
    }).
    otherwise({
        redirectTo: '/'
    });
}]);