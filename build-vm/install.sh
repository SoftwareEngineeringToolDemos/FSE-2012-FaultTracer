sudo apt-get update

#install Java and Maven
sudo apt-get install -y openjdk-7-jdk
sudo apt-get install -y ant

# install git
sudo apt-get install -y git

#install eclipse
sudo wget -nv "http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/mars/1/eclipse-rcp-mars-1-linux-gtk-x86_64.tar.gz&r=1" -O eclipse-rcp-mars-1-linux-gtk-x86_64.tar.gz
cd /home/vagrant && sudo tar xzf /home/vagrant/eclipse-rcp-mars-1-linux-gtk-x86_64.tar.gz
sudo ln -s /home/vagrant/eclipse/eclipse /home/vagrant/Desktop/eclipse

# Clone items to desktop
git clone https://github.com/SoftwareEngineeringToolDemos/FSE-2012-FaultTracer.git /home/vagrant/Desktop/ICSE-2012-FaultTracer

# change permissions
mkdir /home/vagrant/workspace
chmod +x /home/vagrant/Desktop/ICSE-2012-FaultTracer
chmod +x /home/vagrant/eclipse
chmod +x /home/vagrant/workspace

# move files appropriately
mv /home/vagrant/Desktop/ICSE-2012-FaultTracer/build-vm/vm-contents/workspace.tar.gz /home/vagrant/workspace/
mv /home/vagrant/Desktop/ICSE-2012-FaultTracer/build-vm/vm-contents/* /home/vagrant/Desktop/
rm -rf /home/vagrant/Desktop/ICSE-2012-FaultTracer

# Make binary folder and move jar to it
mkdir /home/vagrant/Desktop/Binary
mv /home/vagrant/Desktop/FaultTracer_1.0.0.201510042045.jar /home/vagrant/Desktop/Binary/

# move jar to eclipse plugins
sudo cp /home/vagrant/Desktop/Binary/FaultTracer_1.0.0.201510042045.jar /home/vagrant/eclipse/dropins/

# extract workspace example
sudo tar xzf /home/vagrant/workspace/workspace.tar.gz -C /home/vagrant/workspace

#set up sidebar
sudo rm -f "/usr/share/applications/libreoffice-writer.desktop" 2 > /dev/null
sudo rm -f "/usr/share/applications/libreoffice-calc.desktop" 2 > /dev/null
sudo rm -f "/usr/share/applications/libreoffice-impress.desktop" 2 > /dev/null
sudo rm -f "/usr/share/applications/amazon-default.desktop" 2 > /dev/null
sudo rm -f "/usr/share/applications/ubuntu-software-center.desktop" 2 > /dev/null

# Reboot the machine
sudo reboot