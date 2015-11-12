#How to build your own Virtual Machine?
###The following steps shows how you can spin up a Virtual Machine for tool FaultTracer :

1. Install [vagrant] (https://www.vagrantup.com/downloads.html) and [virtualbox] (https://www.virtualbox.org/wiki/Downloads) on your host machine.
2. Download the [Vagrantfile] (https://github.com/SoftwareEngineeringToolDemos/FSE-2012-FaultTracer/blob/master/build-vm/Vagrantfile) from [build-vm] (https://github.com/SoftwareEngineeringToolDemos/FSE-2012-FaultTracer/blob/master/build-vm) folder on your machine and save it in a folder where you want to install the VM.
3. From the host, navigate to that folder (via bash on Linux Machine or Powershell or CommandPrompt on Windows Machine) and execute the command :  
      "vagrant up"

###Note :
 -  The Virtual Machine will boot up quickly and can be viewed from the Virtual Box but has to wait for the "vagrant up" command for nearly half an hour to complete as it provisions the VM for use.
 -  Deploys Base Vagrant Box : [Ubuntu 14.04 Desktop] (https://vagrantcloud.com/box-cutter/boxes/ubuntu1404-desktop)
 -  Default VM Login Credentials:  
      user: vagrant  
      password: vagrant

###Acknowledgements:

  + A tutorial was followed to introduce Vagrant development from Dustin Barnes, [here](http://www.dev9.com/article/2014/9/dev-environments-with-vagrant).
  + A tutorial was followed to install eclipse from Robert Reiz, [here](http://blog.versioneye.com/2015/05/05/setting-up-a-dev-environment-with-vagrant/).
  