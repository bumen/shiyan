# -*- coding: utf-8 -*-

from locust import HttpLocust, TaskSet, task


class UserTasks(TaskSet):

    def  on_start(self):
        self.index =0

    @task
    def test_visit(self):
        userId = self.locust.share_data[self.index]
        self.index = (self.index+1) % len(self.locust.share_data)
        # url= "/hugo?keyWord=%s" % userId
        url= "/hello?keyWord=%s" % userId
        print("use client url %s" % url)
        self.client.get(url)

class WebsiteUser(HttpLocust):
    """
    Locust user class that does requests to the locust web server running on localhost
    """
    host = "http://127.0.0.1:11010"
    share_data = []
    for index in range(10) :
        share_data.append("R201809131%03d" % index )

    min_wait = 0
    max_wait = 0
    task_set = UserTasks
