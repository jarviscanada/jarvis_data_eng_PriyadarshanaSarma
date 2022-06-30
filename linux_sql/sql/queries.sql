--Returns a round timestamp to the nearest 5 minutes--
CREATE OR REPLACE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

--Group hosts by hardware info, by CPU number and sort by their memory size in descending order(within each cpu_number group)--
SELECT
    cpu_number,
    id AS "host_id",
    total_mem
FROM
    host_info
ORDER BY
    cpu_number,
    total_mem DESC;

--Average memory usage in percentage over 5 mins interval for each host--
SELECT
    hu.host_id,
    hi.hostname,
    round5(hu.timestamp) AS ts,
    AVG(
        (hi.total_mem - hu.memory_free)* 100 / hi.total_mem
        ) AS avg_used_mem_percentage
FROM
    host_info hi,
    host_usage hu
WHERE
        hi.id = hu.host_id
GROUP BY
    ts,
    hu.host_id,
    hi.hostname
ORDER BY
    ts;

--Detect host failure if it inserts less than three data points within 5-min interval in the host_usage table--
SELECT
    host_id,
    round5(timestamp) AS ts,
    COUNT(*) AS "num_data_points"
FROM
    host_usage
GROUP BY
    ts,
    host_id
HAVING
        COUNT(*) < 3
ORDER BY
    ts;