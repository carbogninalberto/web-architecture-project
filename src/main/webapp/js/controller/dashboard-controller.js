routerApp.controller('dashboardViewController',
    function($scope, $rootScope) {
        $rootScope.showNavBar = false;

        $rootScope.hideAll = !$rootScope.sessionInfo.logged;
    });