routerApp.controller('mainViewController',
    function($scope, $rootScope, $http, $interval) {
        $rootScope.showNavBar = true;
        $scope.usage = {
            value: 0,
            time: 0
        };

        $scope.getUsage = function () {
            $http({
                method: 'GET',
                contentType: "application/json; charset=utf-8",
                url: $rootScope.url + '/AssociazioneSportiva-1.0/memory-usage',
                processData: false
            }).then(function success(response) {
                if (response.status == 200) {
                    $scope.usage.value = response.data.usage/(1024*1024.0);
                    $scope.usage.time = response.data.timestamp;
                } else {
                    alert("Error on retrive data.");
                }
            }, function error(response) {
                alert("Error on Callback" + response.data.msg);
            }).catch(function (e) {
                alert(e);
            });
        };

        $interval(function () {
            $scope.getUsage();
        }, 2000);

    });