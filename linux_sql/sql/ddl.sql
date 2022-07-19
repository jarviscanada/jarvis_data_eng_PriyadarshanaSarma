\c host_agent;

--Create host_info table
CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
      id SERIAL NOT NULL PRIMARY KEY,
      hostname VARCHAR NOT NULL,
      cpu_number INTEGER NOT NULL,
      cpu_architecture VARCHAR NOT NULL,
      cpu_model VARCHAR NOT NULL,
      cpu_mhz REAL NOT NULL,
      L2_cache INTEGER NOT NULL,
      total_mem INTEGER NOT NULL,
      "timestamp" TIMESTAMP NOT NULL,
      CONSTRAINT hostname_unique UNIQUE (hostname)
);

--Create host_usage table
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage(
    "timestamp" TIMESTAMP NOT NULL,
    host_id SERIAL NOT NULL,
    memory_free INTEGER NOT NULL,
    cpu_idle INTEGER NOT NULL,
    cpu_kernel INTEGER NOT NULL,
    disk_io INTEGER NOT NULL,
    disk_available INTEGER NOT NULL,
    PRIMARY KEY ("timestamp", host_id),
    CONSTRAINT fk_host_info FOREIGN KEY (host_id) REFERENCES host_info(id)
);