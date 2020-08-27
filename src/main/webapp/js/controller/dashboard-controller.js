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
                newModule: {
                    status: false
                }
            }
        };


        $scope.resetPage = function () {
            $scope.pages.general.profile.status = false;
            $scope.pages.modules.list.status = false;
            $scope.pages.modules.newModule.status = false;
        };

        // page is a string value
        $scope.changePage = function (page) {
            if (page === 'general.profile') {
                $scope.resetPage();
                $scope.pages.general.profile.status = true;
            } else if (page === 'modules.list') {
                $scope.resetPage();
                $scope.pages.modules.list.status = true;
            } else if (page === 'modules.newModule') {
                $scope.resetPage();
                $scope.pages.modules.newModule.status = true;
            }
        };


    });