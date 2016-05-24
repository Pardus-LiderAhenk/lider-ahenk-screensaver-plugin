#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author: Mine Dogan <mine.dogan@agem.com.tr>

import json

from base.plugin.AbstractCommand import AbstractCommand

class Screensaver(AbstractCommand):
    def __init__(self, data, context):
        super(Screensaver, self).__init__()
        self.data = data
        self.context = context

        self.context.put('message_type', 'qwe')
        self.context.put('message_code', 'qwe')
        self.context.put('message', 'qwe')
        self.context.put('data', None)
        self.context.put('content_type', None)

    def handle_policy(self):
        username = self.context.get('username')

        try:
            if username is not None:
                xfile_path = '/home/' + str(username) + '/.xscreensaver'
                xscreensaver_file = open(xfile_path, 'w')

                data = json.loads(self.data)

                xscreensaver_file.write('mode: ' + str(data['mode']) + '\n')
                xscreensaver_file.write('timeout: ' + str(data['timeout']) + '\n')
                xscreensaver_file.write('cycle: ' + str(data['cycle']) + '\n')
                xscreensaver_file.write('lock: ' + str(data['lock']) + '\n')
                xscreensaver_file.write('lockTimeout: ' + str(data['lockTimeout']) + '\n')
                xscreensaver_file.write('grabDesktopImages: ' + str(data['grabDesktopImages']) + '\n')
                xscreensaver_file.write('grabVideoFrames: ' + str(data['grabVideoFrames']) + '\n')
                xscreensaver_file.write('dpmsEnabled: ' + str(data['dpmsEnabled']) + '\n')
                xscreensaver_file.write('dpmsStandby: ' + str(data['dpmsStandby']) + '\n')
                xscreensaver_file.write('dpmsSuspend: ' + str(data['dpmsSuspend']) + '\n')
                xscreensaver_file.write('dpmsOff: ' + str(data['dpmsOff']) + '\n')
                xscreensaver_file.write('dpmsQuickOff: ' + str(data['dpmsQuickOff']) + '\n')
                xscreensaver_file.write('date: ' + str(data['date']) + '\n')
                xscreensaver_file.write('literal: ' + str(data['literal']) + '\n')
                xscreensaver_file.write('textLiteral: ' + str(data['textLiteral']) + '\n')
                xscreensaver_file.write('url: ' + str(data['url']) + '\n')
                xscreensaver_file.write('textUrl: ' + str(data['textUrl']) + '\n')
                xscreensaver_file.write('fade: ' + str(data['fade']) + '\n')
                xscreensaver_file.write('unfade: ' + str(data['unfade']) + '\n')
                xscreensaver_file.write('fadeSeconds: ' + str(data['fadeSeconds']) + '\n')
                xscreensaver_file.write('installColormap: ' + str(data['installColormap']) + '\n')

                xscreensaver_file.close()

                self.set_result('POLICY_STATUS', 'POLICY_PROCESSED', 'User screensaver profile processed successfully.')
                self.logger.info('[Screensaver] Screensaver profile is handled successfully.')

            else:
                self.set_result('POLICY_STATUS', 'POLICY_PROCESSED', 'There is no username.')
        except Exception as e:
            self.logger.error('[Screensaver] A problem occured while handling screensaver profile: {0}'.format(str(e)))
            self.set_result('POLICY_STATUS', 'POLICY_PROCESSED',
                            'A problem occured while handling screensaver profile: {0}'.format(str(e)))


    def set_result(self, type=None, code=None, message=None, data=None, content_type=None):
        self.context.put('message_type', type)
        self.context.put('message_code', code)
        self.context.put('message', message)
        # self.context.put('data')
        # self.context.put('content_type')

def handle_policy(profile_data, context):
    print('SCREENSAVER')
    quota = Screensaver(profile_data, context)
    quota.handle_policy()