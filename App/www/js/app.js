angular.module('ApPosition', ['ionic', 'ngCordova', 'ApPosition.controllers', 'ApPosition.services'])

.run(function($ionicPlatform) {
	$ionicPlatform.ready(function() {
	// Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
	// for form inputs)
	if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
		cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
		cordova.plugins.Keyboard.disableScroll(true);

	}
	if (window.StatusBar) {
		StatusBar.styleDefault();
	}
});
})

.config(function($stateProvider, $urlRouterProvider) {

	// Ionic uses AngularUI Router which uses the concept of states
	// Learn more here: https://github.com/angular-ui/ui-router
	// Set up the various states which the app can be in.
	// Each state's controller can be found in controllers.js
	$stateProvider

	.state('login', {
		url: '/login',
		templateUrl: 'templates/login.html',
		controller: 'LoginCtrl'
	})

	.state('tab', {
		url: '/tab',
		abstract: true,
		templateUrl: 'templates/tabs.html'
	})

	.state('tab.home', {
		url: '/home',
		views: {
			'tab-home': {
				templateUrl: 'templates/tab-home.html',
				controller: 'HomeCtrl'
			}
		}
	})

	.state('tab.meetings', {
		url: '/meetings',
		views: {
			'tab-meetings': {
				templateUrl: 'templates/tab-meetings.html',
				controller: 'MeetingsCtrl'
			}
		}
	})

	.state('tab.meetings/make-meeting', {
		url: "/make-meeting",
		views: {
			'tab-meetings': {
				templateUrl: 'templates/tab-make-meeting.html',
				controller: 'MakeMeetingCtrl'
			}
		}
	})

	.state('tab.settings', {
		url: '/settings',
		views: {
			'tab-settings': {
				templateUrl: 'templates/tab-settings.html',
				controller: 'SettingsCtrl'
			}
		}
	});

	// if none of the above states are matched, use this as the fallback
	$urlRouterProvider.otherwise('/login');

});
