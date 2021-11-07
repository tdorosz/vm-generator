echo "Start provisioning from init.sh"
puttygen /vagrant/.vagrant/machines/default/virtualbox/private_key -o /vagrant/.vagrant/machines/default/virtualbox/private_key.ppk
cp /vagrant/resources/ansible/hosts /etc/ansible/hosts

echo "Done!"