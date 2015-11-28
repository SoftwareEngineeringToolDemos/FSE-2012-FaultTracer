sudo apt-get update

#install Java and Maven
sudo apt-get install -y openjdk-7-jdk
sudo apt-get install -y ant

# install git
sudo apt-get install -y git

#install eclipse
sudo wget -O /opt/eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz http://ftp.fau.de/eclipse/technology/epp/downloads/release/luna/SR2/eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz
cd /opt/ && sudo tar -zxvf eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz

# Clone items to desktop
git clone https://github.com/SoftwareEngineeringToolDemos/FSE-2012-FaultTracer.git /home/vagrant/Desktop/ICSE-2012-FaultTracer

chmod +x /home/vagrant/Desktop/ICSE-2012-FaultTracer
mv /home/vagrant/Desktop/ICSE-2012-FaultTracer/build-vm/vm-contents/* /home/vagrant/Desktop/
rm -rf /home/vagrant/Desktop/ICSE-2012-FaultTracer

# Make binary folder and move jar to it
mkdir /home/vagrant/Desktop/Binary
mv /home/vagrant/Desktop/FaultTracer_1.0.0.201510042045.jar /home/vagrant/Desktop/Binary/FaultTracer_1.0.0.201510042045.jar

# move jar to eclipse plugins
sudo cp /home/vagrant/Desktop/FaultTracer_1.0.0.201510042045.jar /opt/eclipse/plugins/

#set up sidebar
sudo rm -f /user/share/applications/libreoffice-writer.desktop
sudo rm -f /user/share/applications/libreoffice-calc.desktop
sudo rm -f /user/share/applications/libreoffice-impress.desktop
sudo rm -f /user/share/applications/amazon-default.desktop
sudo rm -f /user/share/applications/ubuntu-software-center.desktop

# Reboot the machine
sudo reboot