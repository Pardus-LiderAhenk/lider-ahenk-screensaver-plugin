#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author: Mine Dogan <mine.dogan@agem.com.tr>

import json

from base.plugin.abstract_plugin import AbstractPlugin

class Screensaver(AbstractPlugin):
    def __init__(self, data, context):
        super(Screensaver, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

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
                xscreensaver_file = open(xfile_path, 'w+')

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
                xscreensaver_file.write('textMode: ' + str(data['textMode']) + '\n')
                xscreensaver_file.write('textLiteral: ' + str(data['textLiteral']) + '\n')
                xscreensaver_file.write('textUrl: ' + str(data['textURL']) + '\n')
                xscreensaver_file.write('fade: ' + str(data['fade']) + '\n')
                xscreensaver_file.write('unfade: ' + str(data['unfade']) + '\n')
                xscreensaver_file.write('fadeSeconds: ' + str(data['fadeSeconds']) + '\n')
                xscreensaver_file.write('installColormap: ' + str(data['installColormap']) + '\n')

                xscreensaver_file.close()

                change_owner = 'chown ' + username + ':' + username + ' ' + xfile_path
                self.execute(change_owner)
                self.logger.info('[Screensaver] .xscreensaver owner is changed.')

                self.logger.info('[Screensaver] Screensaver profile is handled successfully.')
                self.context.create_response(code=self.message_code.POLICY_PROCESSED.value,
                                             message='User screensaver profile processed successfully.')

            else:
                self.context.create_response(code=self.message_code.POLICY_ERROR.value,
                                         message='Screensaver profile is only user oriented.')

        except Exception as e:
            self.logger.error('[Screensaver] A problem occured while handling screensaver profile: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.POLICY_ERROR.value, message='A problem occured while handling screensaver profile: {0}'.format(str(e)))


def handle_policy(profile_data, context):
    print('SCREENSAVER')
    screensaver = Screensaver(profile_data, context)
    screensaver.handle_policy()