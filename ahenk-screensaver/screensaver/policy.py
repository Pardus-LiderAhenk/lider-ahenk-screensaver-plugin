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

                content = 'mode: ' + str(data['mode']) + '\n' \
                            'timeout: ' + str(data['timeout']) + '\n' \
                            'cycle: ' + str(data['cycle']) + '\n' \
                            'lock: ' + str(data['lock']) + '\n' \
                            'lockTimeout: ' + str(data['lockTimeout']) + '\n' \
                            'grabDesktopImages: ' + str(data['grabDesktopImages']) + '\n' \
                            'grabVideoFrames: ' + str(data['grabVideoFrames']) + '\n' \
                            'dpmsEnabled: ' + str(data['dpmsEnabled']) + '\n' \
                            'dpmsStandby: ' + str(data['dpmsStandby']) + '\n' \
                            'dpmsSuspend: ' + str(data['dpmsSuspend']) + '\n' \
                            'dpmsOff: ' + str(data['dpmsOff']) + '\n' \
                            'dpmsQuickOff: ' + str(data['dpmsQuickOff']) + '\n' \
                            'date: ' + str(data['date']) + '\n' \
                            'literal: ' + str(data['literal']) + '\n' \
                            'textLiteral: ' + str(data['textLiteral']) + '\n' \
                            'url: ' + str(data['url']) + '\n' \
                            'textUrl: ' + str(data['textUrl']) + '\n' \
                            'fade: ' + str(data['fade']) + '\n' \
                            'unfade: ' + str(data['unfade']) + '\n' \
                            'fadeSeconds: ' + str(data['fadeSeconds']) + '\n' \
                            'installColormap: ' + str(data['installColormap']) + '\n'
                
                xscreensaver_file.write(content)
                xscreensaver_file.close()

                self.logger.debug('[Screensaver] Creating response.')
                self.context.create_response(code=self.get_message_code().POLICY_PROCESSED.value,
                                             message='Screensaver policy executed successfully')
                self.logger.info('[Screensaver] Screensaver profile is handled successfully.')

            else:
                raise Exception('There is no username.')

        except Exception as e:
            self.logger.error('[Screensaver] A problem occurred while handling Screensaver policy. Error Message: {}'.format(str(e)))
            self.context.create_response(code=self.get_message_code().POLICY_ERROR.value,
                                         message='A problem occurred while handling Screensaver policy')


def handle_policy(profile_data, context):
    screensaver = Screensaver(profile_data, context)
    screensaver.handle_policy()