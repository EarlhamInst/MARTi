package uk.ac.earlham.lcaparse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxonomyNodeTest {

    @Test
    void childrenAndParentTrackingWorkAsExpected() {
        TaxonomyNode<?> node = new TaxonomyNode<>(100L);
        assertEquals(100L, node.getId());
        assertTrue(node.isLeafNode(), "A new node should be a leaf until children are added");

        TaxonomyNode<?> child = new TaxonomyNode<>(101L);
        node.addChild(child);
        assertFalse(node.isLeafNode(), "A node with children should no longer be a leaf");
        assertEquals(1, node.getChildren().size());
        assertSame(child, node.getChildren().get(0));

        node.setParent(200L);
        assertEquals(200L, node.getParent());

        node.setDisplayPosition(5, 7);
        assertEquals(5, node.getDisplayCol());
        assertEquals(7, node.getDisplayRow());
    }

    @Test
    void assignmentAndSummedYieldsAccumulateAndReportCorrectly() {
        TaxonomyNode<?> node = new TaxonomyNode<>(100L);
        node.incrementAssignedAndAddYield(3, 42L);
        node.incrementAssignedAndAddYield(3, 42L);

        assertEquals(2, node.getAssigned(3));
        assertEquals(84L, node.getAssignedYield(3));
        assertEquals(0, node.getAssigned(5));

        node.incrementSummedAndAddYield(3, 5L);
        assertEquals(1, node.getSummed(3));
        assertEquals(5L, node.getSummedYield(3));
    }

    @Test
    void lcaCountsAndZeroingMaintainExpectedState() {
        TaxonomyNode<?> node = new TaxonomyNode<>(100L);
        node.incrementAssignedAndAddYield(7, 12L);
        node.incrementSummedAndAddYield(7, 5L);
        node.setLCACountsToMatch(7);

        assertEquals(1, node.getLCAAssigned(7));
        assertEquals(1, node.getLCASummed(7));
        assertEquals(12L, node.getLCAYield(7));
        assertEquals(5L, node.getLCASummedYield(7));

        node.addToLCAAssigned(7, 3, 8L);
        assertEquals(4, node.getLCAAssigned(7));
        assertEquals(20L, node.getLCAYield(7));

        node.zeroLCACounts(7);
        assertEquals(0, node.getLCAAssigned(7));
        assertEquals(0L, node.getLCAYield(7));
        assertEquals(0, node.getLCASummed(7));
        assertEquals(0L, node.getLCASummedYield(7));

        node.zeroLCAAssignedCount(7);
        node.zeroLCASummmmarisedCount(7);
        assertEquals(0, node.getLCAAssigned(7));
        assertEquals(0, node.getLCASummed(7));
    }
}
