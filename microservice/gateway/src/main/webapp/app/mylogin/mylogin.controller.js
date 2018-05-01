(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('myLoginCtr', myLoginCtr);

    myLoginCtr.$inject = ['$scope','$rootScope', '$state', '$timeout', 'Auth'];

    function myLoginCtr ($scope,$rootScope, $state, $timeout, Auth) {
        //var vm = this;
        $scope.vm = {};
        $scope.vm.authenticationError = false;
        $scope.vm.cancel = cancel;
        $scope.vm.credentials = {};
        $scope.vm.login = login;
        $scope.vm.password = null;
        $scope.vm.register = register;
        $scope.vm.rememberMe = true;
        $scope.vm.requestResetPassword = requestResetPassword;
        $scope.vm.username = null;

        $timeout(function (){angular.element('#username').focus();});

        function cancel () {
            $scope.vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            $scope.vm.authenticationError = false;
            //$uibModalInstance.dismiss('cancel');
        }

        function login (event) {
            Auth.logout();
            event.preventDefault();
            Auth.login({
                username: $scope.vm.username,
                password: $scope.vm.password,
                rememberMe: $scope.vm.rememberMe
            }).then(function () {
                $scope.vm.authenticationError = false;
                //$uibModalInstance.close();
                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
            }).catch(function () {
                $scope.vm.authenticationError = true;
            });
        }

        function register () {
            //$uibModalInstance.dismiss('cancel');
            $state.go('register');
        }

        function requestResetPassword () {
            //$uibModalInstance.dismiss('cancel');
            $state.go('requestReset');
        }
    }
})();
