#!/bin/bash

#echo "==============================="
#echo "INSTALLING ANSIBLE DEPENDENCIES"
#echo "-------------------------------"
apt-get update
apt-get install -y language-pack-en python-pip python-paramiko libffi-dev libssl-dev python-dev
sudo pip install markupsafe
sudo pip install setuptools
sudo sh -c 'echo "ubuntu ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers' # see https://askubuntu.com/questions/192050/how-to-run-sudo-command-with-no-password

#echo "=================="
#echo "INSTALLING ANSIBLE"
#echo "------------------"
sudo pip install ansible

#echo "=========================================="
#echo "RUNNING PLAYBOOKS WITH ANSIBLE*"
#echo "------------------------------------------"

cd /vagrant/vagrant
ansible-playbook -i provisioning/inventory provisioning/nsq-spark.yml --connection=local --sudo

