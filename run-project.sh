echo '------- Starting postgrest with Docker ----------'
sudo docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5433:5432 -d postgres

echo '------- Cloning project ----------'
git clone https://github.com/DanielDi/praxis-gildedrose.git

echo '------- Run project ----------'
cd /home/vagrant/praxis-gildedrose
mvn spring-boot:run