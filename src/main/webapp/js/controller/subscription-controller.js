routerApp.controller('subscriptionController',
    function($scope, $rootScope) {
        $rootScope.showNavBar = false;
        $rootScope.hideAll = !$rootScope.sessionInfo.logged;

        $scope.subscriptionStatus = 'INFORMATION';
        $scope.paymentGateway = null;

        // server call
        $scope.setSubscriptionStatus = function () {
            $http({
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/status',
                data: {
                    email: $rootScope.sessionInfo.utente.email,
                    paymentGateway: $scope.paymentGateway
                },
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.subscriptionStatus = response.data.subscriptionStatus;
                } else {
                    alert("Error on retrive status of subscription.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        }

        $scope.setSubscriptionStatus = function () {
            $http({
                method: 'GET',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/status',
                data: {
                    email: $rootScope.sessionInfo.utente.email,
                    paymentGateway: $scope.paymentGateway
                },
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.subscriptionStatus = response.data.subscriptionStatus;
                } else {
                    alert("Error on retrive status of subscription.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        }


    });