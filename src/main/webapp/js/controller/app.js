var routerApp = angular.module('app', ['ngRoute', 'ngCookies']);
routerApp.controller('appController',
    function($scope, $rootScope, $http, $timeout, $location, $route, $cookies, $window) {
        // logo-name
        $scope.appName = "Sportif";
        // show-hide homepage nav-bar
        $rootScope.showNavBar = true;
        $rootScope.hideAll = false;

        $rootScope.url = "";

        $rootScope.sessionInfo = {
            logged: false,
            utente: {
                admin: false
            }
        };

        if (localStorage.getItem('sessionInfo') != null)
            $rootScope.sessionInfo = JSON.parse(localStorage.getItem('sessionInfo'));

        $scope.resetNav = function () {
            $rootScope.showNavBar = true;
            $rootScope.hideAll = false;
            $location.url('/');
        }
        $scope.logout = function () {

            $http({
                method: 'DELETE',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/session/delete',
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {

                    $rootScope.sessionInfo.logged = false;
                    $rootScope.sessionInfo.utente = {admin: false};

                    //$cookies.remove("");
                    $window.localStorage.clear();

                    // redirect after some seconds
                    $timeout(function () {
                        $location.url('/');
                    }, 1500);

                } else {
                    $scope.error = {
                        status: true,
                        msg: response.data.msg
                    }
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        }

        /*
        $scope.logout = function () {
            var cookies = $cookies.getAll();
            angular.forEach(cookies, function (v, k) {
                $cookies.remove(k);
            });
            $timeout(function () {
                $location.url('/dashboard');
            }, 2000);
        };

         */
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
    when('/dashboard', {
        templateUrl: 'views/dashboard-template.html',
        controller: 'dashboardViewController'
    }).
    otherwise({
        redirectTo: '/'
    });
}]);