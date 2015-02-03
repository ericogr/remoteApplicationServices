angular.module('ras.traducaoMensagens', ['pascalprecht.translate'])
    .config(function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'resources/i18n/locale-',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('pt_BR');
    });