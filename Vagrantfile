# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "hashicorp/bionic64"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  config.vm.network "forwarded_port", guest: 8080, host: 8090

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80, host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  # config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
  #   # Customize the amount of memory on the VM:
  #   vb.memory = "1024"
  # end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Ansible, Chef, Docker, Puppet and Salt are also available. Please see the
  # documentation for more information about their specific syntax and use.
    config.vm.provision "shell", inline: <<-SHELL
      sudo apt-get update

      echo "----- Installing Apache and Java 17 ------"
      mkdir -p /usr/share/man/man1
      sudo apt-get -y install openjdk-17-jdk

      export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
      export PATHADD=$JAVA_HOME/bin
      export PATH=$PATH:$PATHADD

      echo "----- Installing Maven------"
      wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz
      tar -xvf apache-maven-3.8.5-bin.tar.gz
      sudo mv apache-maven-3.8.5 /usr/lib/

      export M2_HOME=/usr/lib/apache-maven-3.8.5
      export MAVEN_HOME=/usr/lib/apache-maven-3.8.5
      export PATH=${M2_HOME}/bin:${PATH}

      sudo apt-get install -y docker.io

      echo '------- Starting postgrest with Docker ----------'
      sudo docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5433:5432 -d postgres

      echo '------- Cloning project ----------'
      git clone https://github.com/DanielDi/praxis-gildedrose.git

      echo '------- Run project ----------'
      cd /home/vagrant/praxis-gildedrose
      mvn spring-boot:run
    SHELL
#   config.vm.provision "shell", path: "settings.sh"
#   config.vm.provision :docker
#   config.vm.provision "shell", path: "run-project.sh"
end