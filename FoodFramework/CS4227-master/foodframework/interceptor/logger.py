import os
import logging


class Logger:
    """
    Custom Logger used within the interceptor pattern.
    """

    def __init__(self):
        self.filename = 'foodframework\\logs\\logger.log'
        formatter = logging.Formatter('%(asctime)s:%(levelname)s:%(message)s')
        self.logger = logging.getLogger(__name__)
        self.logger.setLevel(logging.DEBUG)
        self.file_handler = logging.FileHandler(self.filename)
        self.file_handler.setFormatter(formatter)

        # allows for writing to the console.
        self.stream_handler = logging.StreamHandler()
        self.stream_handler.setFormatter(formatter)

        self.logger.addHandler(self.file_handler)
        self.logger.addHandler(self.stream_handler)
        # Basic logging
        # format = '%(asctime)s:%(levelname)s:%(message)s'
        # logging.basicConfig(filename=self.filename, level=logging.DEBUG, format=format)

    def log(self, msg: str, level: int):
        self.logger.log(level=level, msg=msg)

    def debug(self, msg: str):
        self.logger.debug(msg=msg)

    def info(self, msg: str):
        self.logger.info(msg=msg)

    def error(self, msg: str):
        self.logger.error(msg=msg)

    def warning(self, msg: str):
        self.logger.warning(msg=msg)

    def critical(self, msg: str):
        self.logger.critical(msg=msg)

    def set_file(self, filename: str):
        self.filename = filename

    def set_format(self, new_format: str):
        # self.py_logger.__format__(new_format)
        pass
