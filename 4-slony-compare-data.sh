#!/bin/bash
source ./setEnv.sh
echo -n "**** comparing sample1 ... "
psql $MST_DB_REPL_CNN $MASTERDBNAME >dump.tmp.1.$$ <<_EOF_
        select 'pgbench_accounts:'::text, aid, bid, abalance, filler
            from pgbench_accounts order by aid;
        select 'pgbench_branches:'::text, bid, bbalance, filler
            from pgbench_branches order by bid;
        select 'pgbench_tellers:'::text, tid, bid, tbalance, filler
            from pgbench_tellers order by tid;
        select 'pgbench_history:'::text, tid, bid, aid, delta, mtime, filler,
            id
            from pgbench_history order by id;
_EOF_
psql $SLV_DB_REPL_CNN $SLAVEDBNAME >dump.tmp.2.$$ <<_EOF_
        select 'pgbench_accounts:'::text, aid, bid, abalance, filler
            from pgbench_accounts order by aid;
        select 'pgbench_branches:'::text, bid, bbalance, filler
            from pgbench_branches order by bid;
        select 'pgbench_tellers:'::text, tid, bid, tbalance, filler
            from pgbench_tellers order by tid;
        select 'pgbench_history:'::text, tid, bid, aid, delta, mtime, filler,
            id
            from pgbench_history order by id;
_EOF_

if diff dump.tmp.1.$$ dump.tmp.2.$$ >$CLUSTERNAME.diff ; then
        echo "success - databases are equal."
        rm dump.tmp.?.$$
        rm $CLUSTERNAME.diff
else
        echo "FAILED - see $CLUSTERNAME.diff for database differences"
fi