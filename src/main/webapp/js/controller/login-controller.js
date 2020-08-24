routerApp.controller('loginViewController',
    function($scope, $rootScope, $http, $location, $timeout) {
        $rootScope.showNavBar = true;

        $scope.error = {
            status: false,
            msg: ""
        };
        $scope.success = {
            status: false,
            msg: ""
        };
        $scope.utente = {};

        $scope.reset = function () {
            $scope.error = {
                status: false,
                msg: ""
            }
            $scope.success = {
                status: false,
                msg: ""
            }
        }

        $scope.login = function () {
            // resetting error box
            $scope.reset();

            $http({
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                url: '/AssociazioneSportiva-1.0/login',
                data: {
                    email: $scope.utente.email,
                    password: $scope.utente.password
                },
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.success = {
                        status: true,
                        msg: response.data.msg
                    }

                    $rootScope.sessionInfo.logged = true;
                    $rootScope.sessionInfo.utente = response.data.utente;

                    //save to localStorage
                    localStorage.setItem('sessionInfo', JSON.stringify($rootScope.sessionInfo));

                    // redirect after some seconds
                    $timeout(function () {
                        $location.url('/dashboard');
                    }, 2000);

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

    });