(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('mylogin', {
            url: '/mylogin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mylogin'
            },
            views: {
                'content@': {
                    templateUrl: 'app/mylogin/mylogin.html',
                    controller: 'myLoginCtr'
                    //controllerAs: 'vm'
                }
            }
        });
    }
})();
