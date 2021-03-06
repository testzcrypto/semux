/**
 * Copyright (c) 2017 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.semux.gui.AddressBook.Entry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddressBookTest {

    private static final String NAME1 = "testname";
    private static final String ADDRESS1 = "0fc1ea0571e639a8f83a9434d65326ea43b2fd00";
    private static final String NAME2 = "two";
    private static final String ADDRESS2 = "0f1111111111111111111111111111111111111";

    private AddressBook ab;

    @Before
    public void setUp() {
        ab = new AddressBook(new File("addressbook.json"));
    }

    @Test
    public void testPut1() {
        ab.put(new Entry(NAME1, ADDRESS1));
        ab.put(new Entry(NAME2, ADDRESS2));

        assertEquals(ADDRESS1, ab.getByName(NAME1).getAddress());
        assertEquals(ADDRESS2, ab.getByName(NAME2).getAddress());

        List<Entry> all = ab.list();
        Optional<Entry> first = all.stream().filter(a -> a.getName().equals(NAME1)).findAny();
        Optional<Entry> second = all.stream().filter(a -> a.getName().equals(NAME2)).findAny();

        assertTrue(first.isPresent());
        assertTrue(second.isPresent());

        assertEquals(ADDRESS1, first.get().getAddress());
        assertEquals(ADDRESS2, second.get().getAddress());
    }

    @Test
    public void testPut2() {
        assertNull(ab.getByName(NAME1));
        assertNull(ab.getByName(NAME2));

        ab.put(NAME1, ADDRESS1);
        assertEquals(ADDRESS1, ab.getByName(NAME1).getAddress());
        ab.remove(NAME1);
        assertNull(ab.getByName(NAME1));

        ab.put(NAME2, ADDRESS2);
        assertEquals(ADDRESS2, ab.getByName(NAME2).getAddress());
        ab.remove(NAME2);
        assertNull(ab.getByName(NAME2));
    }

    @Test
    public void testUpdate() {
        ab.put(new Entry(NAME1, ADDRESS1));
        assertEquals(ADDRESS1, ab.getByName(NAME1).getAddress());

        Entry entry = new Entry(NAME1, ADDRESS2);
        ab.put(entry);
        assertEquals(ADDRESS2, ab.getByName(NAME1).getAddress());

        ab.remove(NAME1);
        assertNull(ab.getByName(NAME1));
    }

    @Test
    public void testRemoveNotExistent() {
        ab.remove("unknownEntry");
        assertNull(ab.getByName("unknownEntry"));
    }

    @Test
    public void testPutWithStrings() {
        String username = "TestMan";
        String address = "0fc1ea0571e639a8f83a9434d65326ea43b2fd00";
        ab.put(username, address);

        Entry entry = ab.getByName(username);
        assertEquals(username, entry.getName());
        assertEquals(address, entry.getAddress());

        ab.remove(username);
        assertNull(ab.getByName(username));
    }

    @Test
    public void testEntry() {
        Map<Entry, Integer> map = new HashMap<>();

        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Entry e = new Entry(Integer.toString(i), Integer.toString(i));
            map.put(e, i);
            list.add(e);
        }

        for (Entry e : list) {
            assertEquals(e.getAddress(), map.get(e).toString());
        }
    }

    @After
    public void tearDown() {
        ab.clear();
    }
}