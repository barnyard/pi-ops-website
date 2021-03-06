package com.bt.pi.ops.website.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.bt.pi.app.common.entities.AvailabilityZone;
import com.bt.pi.app.common.entities.AvailabilityZones;
import com.bt.pi.app.common.entities.Region;
import com.bt.pi.app.common.entities.Regions;
import com.bt.pi.app.common.id.PiIdBuilder;
import com.bt.pi.core.application.health.entity.HeartbeatEntityCollection;
import com.bt.pi.core.dht.cache.BlockingDhtCache;
import com.bt.pi.core.id.PId;
import com.bt.pi.core.parser.KoalaJsonParser;
import com.bt.pi.ops.website.reporting.HeartbeatRetriever;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatsControllerTest {
	private String json = "json";
	private int availabilityZoneCode = 1;
	private int regionCode = 2;

	@Mock
	private PId availabilityZonesId;
	@Mock
	private PId regionsId;

	@Mock
	private HeartbeatEntityCollection heartbeatEntityCollection;
	@Mock
	private HeartbeatRetriever heartbeatRetriever;
	@Mock
	private KoalaJsonParser koalaJsonParser;
	@Mock
	private PiIdBuilder piIdBuilder;
	@Mock
	private BlockingDhtCache blockingDhtCache;

	@InjectMocks
	private HeartbeatsController heartbeatsController = new HeartbeatsController();

	@Before
	public void setup() {
		when(piIdBuilder.getAvailabilityZonesId()).thenReturn(availabilityZonesId);
		when(piIdBuilder.getRegionsId()).thenReturn(regionsId);

		Regions regions = new Regions();
		regions.addRegion(new Region("r1", regionCode, null, null));

		AvailabilityZones availabilityZones = new AvailabilityZones();
		availabilityZones.addAvailabilityZone(new AvailabilityZone("av1", availabilityZoneCode, regionCode, null));

		when(blockingDhtCache.get(availabilityZonesId)).thenReturn(availabilityZones);
		when(blockingDhtCache.get(regionsId)).thenReturn(regions);

		when(koalaJsonParser.getJson(heartbeatEntityCollection)).thenReturn(json);
	}

	@Test
	public void shouldGetHeartbeats() throws Exception {
		// setup
		when(heartbeatRetriever.getAllHeartbeats(regionCode, availabilityZoneCode)).thenReturn(heartbeatEntityCollection);

		// act
		String result = heartbeatsController.getHeartbeats("av1");

		// assert
		assertThat(result, equalTo(json));
	}
}
