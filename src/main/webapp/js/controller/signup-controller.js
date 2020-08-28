routerApp.controller('signupViewController',
    function($scope, $http, $rootScope) {

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

        $scope.signup = function () {
            if ($scope.utente.password === $scope.utente.confirm) {
                // resetting error box
                $scope.reset();

                $http({
                    method: 'POST',
                    contentType: "application/json; charset=utf-8",
                    url: $rootScope.url + '/AssociazioneSportiva-1.0/user/add',
                    data: {
                        email: $scope.utente.email,
                        name: $scope.utente.name,
                        password: $scope.utente.password
                    },
                    processData: false
                }).then(function success(response) {
                    if (response.status == 200) {
                        $scope.success = {
                            status: true,
                            msg: response.data.msg
                        }
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
                })
            } else {
                // showing error box
                $scope.error.status = true;
                $scope.error.msg = "Password doesn't match!";
            }

        }

    });