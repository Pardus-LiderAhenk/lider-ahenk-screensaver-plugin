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

                content = 'mode: ' + str(data['mode']) + '\n' \
                                                         'timeout: ' + str(data['timeout']) + '\n' \
                                                                                              'cycle: ' + str(
                    data['cycle']) + '\n' \
                                     'lock: ' + str(data['lock']) + '\n' \
                                                                    'lockTimeout: ' + str(data['lockTimeout']) + '\n' \
                                                                                                                 'grabDesktopImages: ' + str(
                    data['grabDesktopImages']) + '\n' \
                                                 'grabVideoFrames: ' + str(data['grabVideoFrames']) + '\n' \
                                                                                                      'dpmsEnabled: ' + str(
                    data['dpmsEnabled']) + '\n' \
                                           'dpmsStandby: ' + str(data['dpmsStandby']) + '\n' \
                                                                                        'dpmsSuspend: ' + str(
                    data['dpmsSuspend']) + '\n' \
                                           'dpmsOff: ' + str(data['dpmsOff']) + '\n' \
                                                                                'dpmsQuickOff: ' + str(
                    data['dpmsQuickOff']) + '\n' \
                                            'textMode: ' + str(data['textMode']) + '\n' \
                                                                                   'textLiteral: ' + str(
                    data['textLiteral']) + '\n' \
                                           'textUrl: ' + str(data['textUrl']) + '\n' \
                                                                                'fade: ' + str(data['fade']) + '\n' \
                                                                                                               'unfade: ' + str(
                    data['unfade']) + '\n' \
                                      'fadeSeconds: ' + str(data['fadeSeconds']) + '\n' \
                                                                                   'installColormap: ' + str(
                    data['installColormap']) + '\n'

                xscreensaver_file.write(content)
                xscreensaver_file.close()

                change_owner = 'chown ' + username + ':' + username + ' ' + xfile_path
                self.execute(change_owner)
                self.logger.info('.xscreensaver owner is changed.')

                self.logger.info('Screensaver profile is handled successfully.')
                self.context.create_response(code=self.message_code.POLICY_PROCESSED.value,
                                             message='Kullanıcı screensaver profili başarıyla çalıştırıldı.')

            else:
                self.context.create_response(code=self.message_code.POLICY_ERROR.value,
                                             message='Screensaver profili sadece kullanıcı tabanlıdır.')

        except Exception as e:
            self.logger.error('A problem occured while handling screensaver profile: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.POLICY_ERROR.value,
                                         message='Screensaver profili çalıştırılırken bir hata oluştu.')


def handle_policy(profile_data, context):
    screensaver = Screensaver(profile_data, context)
    screensaver.handle_policy()
