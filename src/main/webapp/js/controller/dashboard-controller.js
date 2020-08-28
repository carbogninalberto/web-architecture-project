routerApp.controller('dashboardViewController',
    function($scope, $rootScope) {
        $rootScope.showNavBar = false;
        $rootScope.hideAll = !$rootScope.sessionInfo.logged;

        $scope.pages = {
            general: {
                profile: {
                    status: true
                }
            },
            modules: {
                list: {
                    status: false
                },
            subscription: {
                    status: false
                }
            }
        };

        $scope.resetPage = function () {
            $scope.pages.general.profile.status = false;
            $scope.pages.modules.list.status = false;
            $scope.pages.modules.subscription.status = false;
        };

        // page is a string value
        $scope.changePage = function (page) {
            if (page === 'general.profile') {
                $scope.resetPage();
                $scope.pages.general.profile.status = true;
            } else if (page === 'modules.list') {
                $scope.resetPage();
                $scope.pages.modules.list.status = true;
            } else if (page === 'modules.subscription') {
                $scope.resetPage();
                $scope.pages.modules.subscription.status = true;
            }
        };


    });