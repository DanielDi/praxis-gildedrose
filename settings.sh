sudo apt-get update
echo "----- Installing Apache and Java 17 ------"
mkdir -p /usr/share/man/man1
sudo apt-get -y install openjdk-17-jdk

echo 'export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64' >> /home/vagrant/.bashrc
echo 'export PATHADD=$JAVA_HOME/bin' >> /home/vagrant/.bashrc
echo 'export PATH=$PATH:$PATHADD' >> /home/vagrant/.bashrc

export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
export PATHADD=$JAVA_HOME/bin
export PATH=$PATH:$PATHADD

echo "----- Installing Maven------"
wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
tar -xvf apache-maven-3.8.5-bin.tar.gz
sudo mv apache-maven-3.8.5 /usr/lib/

echo 'export M2_HOME=/usr/lib/apache-maven-3.8.5' >> /home/vagrant/.bashrc
echo 'export MAVEN_HOME=/usr/lib/apache-maven-3.8.5' >> /home/vagrant/.bashrc
echo 'export PATH=${M2_HOME}/bin:${PATH}' >> /home/vagrant/.bashrc

export M2_HOME=/usr/lib/apache-maven-3.8.5
export MAVEN_HOME=/usr/lib/apache-maven-3.8.5
export PATH=${M2_HOME}/bin:${PATH}