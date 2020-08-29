routerApp.controller('subscriptionController',
    function($scope, $rootScope, $http) {
        $rootScope.showNavBar = false;
        $rootScope.hideAll = !$rootScope.sessionInfo.logged;

        $scope.subscriptionStatus = 'INFORMATION';

        $scope.gateways = [
            { name: "VISA" },
            { name: "MASTERCARD" },
            { name: "TRANSFER" }
        ];

        $scope.paymentGateway = {
            selected: $scope.gateways[0].name
        };
        $scope.subscriptionInfo = {
            name: "",
            last: "",
            date: "",
            privacy: 0
        };

        $scope.subscriptions = [];

        $scope.addSubscription = function () {
            $http({
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/add',
                data: {
                    email: $rootScope.sessionInfo.utente.email,
                    name: $scope.subscriptionInfo.name,
                    lastName: $scope.subscriptionInfo.last,
                    born: $scope.subscriptionInfo.date.getTime()
                },
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.subscriptionStatus = response.data.subscriptionStatus;
                    $scope.paymentGateway.selected = response.data.paymentGateway;
                } else {
                    alert("Error on retrive status of subscription.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        };

        $scope.setSubscriptionStatus = function () {
            $http({
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/status',
                data: {
                    email: $rootScope.sessionInfo.utente.email,
                    paymentGateway: $scope.paymentGateway.selected
                },
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.subscriptionStatus = response.data.subscriptionStatus;
                    $scope.paymentGateway.selected = response.data.paymentGateway;
                } else {
                    alert("Error on retrive status of subscription.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        };

        $scope.getSubscriptionStatus = function () {
            $http({
                method: 'GET',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/status',
                params: {
                    email: $rootScope.sessionInfo.utente.email
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
        };

        $scope.getSubscriptions = function () {
            $http({
                method: 'GET',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/subscription/list',
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.subscriptions = response.data.subscriptions;
                } else {
                    alert("Error on retrive subscriptions.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        };

        $scope.getSubscriptions();
        $scope.getSubscriptionStatus();


    });