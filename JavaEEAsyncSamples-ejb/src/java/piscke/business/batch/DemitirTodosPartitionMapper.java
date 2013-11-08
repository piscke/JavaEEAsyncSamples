/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscke.business.batch;

import piscke.business.dao.FuncionarioDAO;
import java.util.Properties;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.context.JobContext;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author leandro.piscke
 */
@Dependent
@Named("DemitirTodosPartitionMapper")
public class DemitirTodosPartitionMapper implements PartitionMapper {

    @EJB
    FuncionarioDAO funcionarioDAO;

    @Inject
    JobContext jobCtx;

    @Override
    public PartitionPlan mapPartitions() throws Exception {

        return new PartitionPlanImpl() {

            /* The number of partitions could be dynamically calculated based on
             * many parameters. In this particular example, we are setting it to
             * a fixed value for simplicity.
             */
            @Override
            public int getPartitions() {
                return Integer.parseInt(jobCtx.getProperties().getProperty("partitions"));
            }

            /* Obtaint the parameters for each partition. In this case,
             * the parameters represent the range of items each partition
             * of the step should work on.
             */
            @Override
            public Properties[] getPartitionProperties() {

                /* Assign an (approximately) equal number of elements
                 * to each partition. */
                long totalItems = funcionarioDAO.contagem();
                long partItems = (long) totalItems / getPartitions();
                long remItems = totalItems % getPartitions();

                /* Populate a Properties array. Each Properties element
                 * in the array corresponds to each partition. */
                Properties[] props = new Properties[getPartitions()];

                for (int i = 0; i < getPartitions(); i++) {
                    props[i] = new Properties();
                    props[i].put("firstItem", i * partItems);
                    /* Last partition gets the remainder elements */
                    if (i == getPartitions() - 1) {
                        props[i].put("numItems", partItems + remItems);
                    } else {
                        props[i].put("numItems", partItems);
                    }
                }

                return props;
            }
        };
    }
}
