package com.example.firstproject.chime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.Attendee;
import com.amazonaws.services.chime.model.CreateAttendeeRequest;
import com.amazonaws.services.chime.model.CreateAttendeeResult;
import com.amazonaws.services.chime.model.CreateMeetingRequest;
import com.amazonaws.services.chime.model.CreateMeetingResult;
import com.amazonaws.services.chime.model.DeleteAttendeeRequest;
import com.amazonaws.services.chime.model.DeleteMeetingRequest;
import com.amazonaws.services.chime.model.Meeting;

@RestController
@CrossOrigin(origins = "*")
public class MeetingSchedulingService {

	private AmazonChime meetingClient = AwsMeetingClient.getMeetingClient();
	private Map<String, Meeting> meetingCache = new HashMap<String, Meeting>();
	private Map<String, Attendee> attendeeCache = new HashMap<String, Attendee>();

	@GetMapping("/getMeeting")
	public ResponseEntity<Meeting> getMeeting(@RequestParam(name = "host") String host) {
		if (meetingCache.containsKey(host)) {
			return ResponseEntity.ok(meetingCache.get(host));
		}
		return createMeeting(host);
	}

	@PostMapping("/createMeeting")
	public ResponseEntity<Meeting> createMeeting(@RequestParam(name = "host") String host) {
		if (meetingCache.containsKey(host)) {
			return ResponseEntity.ok(meetingCache.get(host));
		}

		CreateMeetingRequest request = new CreateMeetingRequest().withMediaRegion("ap-southeast-1")
				.withExternalMeetingId(UUID.randomUUID().toString())
				.withClientRequestToken(UUID.randomUUID().toString()).withMeetingHostId(host)
				.withRequestCredentialsProvider(new CredentialProvider());
		CreateMeetingResult result = meetingClient.createMeeting(request);
		meetingCache.put(host, result.getMeeting());
		return ResponseEntity.ok(meetingCache.get(host));

	}

	@PostMapping("/createAttendee")
	public ResponseEntity<Attendee> createAttendee(@RequestParam(name = "meetingId") String meetingId,
			@RequestParam(name = "user") String user) {
		if (meetingCache.values().stream().noneMatch(meeting -> meeting.getMeetingId().equals(meetingId))) {
			return ResponseEntity.badRequest().header("Error Message", "No active meeting with meeting id").build();
		}

		if (attendeeCache.containsKey(user)) {
			return ResponseEntity.ok(attendeeCache.get(user));
		}
		CreateAttendeeRequest request = new CreateAttendeeRequest().withMeetingId(meetingId)
				.withRequestCredentialsProvider(new CredentialProvider());
		CreateAttendeeResult result = meetingClient.createAttendee(request);
		attendeeCache.put(user, result.getAttendee());
		return ResponseEntity.ok(attendeeCache.get(user));

	}

	@PostMapping("/deleteMeeting")
	public ResponseEntity<Void> deleteMeeting(@RequestParam(name = "meetingId") String meetingId) {
		if (meetingCache.values().stream().noneMatch(meeting -> meeting.getMeetingId().equals(meetingId))) {
			return ResponseEntity.badRequest().header("Error Message", "No active meeting with meeting id").build();
		}
		DeleteMeetingRequest request = new DeleteMeetingRequest().withMeetingId(meetingId)
				.withRequestCredentialsProvider(new CredentialProvider());
		meetingClient.deleteMeeting(request);
		removeMeeting(meetingId);
		return ResponseEntity.noContent().build();

	}

	@PostMapping("/deleteAttendee")
	public ResponseEntity<Void> deleteAttendee(@RequestParam(name = "meetingId") String meetingId,
			@RequestParam(name = "user") String user) {
		if (meetingCache.values().stream().noneMatch(meeting -> meeting.getMeetingId().equals(meetingId))) {
			return ResponseEntity.badRequest().header("Error message", "No active meeting with meeting id").build();
		}
		if (!attendeeCache.containsKey(user)) {
			return ResponseEntity.badRequest().header("Error message", "No active attendee for user").build();
		}
		DeleteAttendeeRequest request = new DeleteAttendeeRequest().withMeetingId(meetingId)
				.withAttendeeId(attendeeCache.get(user).getAttendeeId())
				.withRequestCredentialsProvider(new CredentialProvider());
		meetingClient.deleteAttendee(request);
		removeAttendee(attendeeCache.get(user).getAttendeeId());
		return ResponseEntity.noContent().build();

	}

	private void removeAttendee(String attendeeId) {
		Iterator<Attendee> iterator = attendeeCache.values().iterator();
		while (iterator.hasNext()) {
			Attendee attendee = iterator.next();
			if (attendee.getAttendeeId().equals(attendeeId)) {
				iterator.remove();
				break;
			}
		}
	}

	private void removeMeeting(String meetingId) {
		Iterator<Meeting> iterator = meetingCache.values().iterator();
		while (iterator.hasNext()) {
			Meeting meeting = iterator.next();
			if (meeting.getMeetingId().equals(meetingId)) {
				iterator.remove();
				break;
			}
		}
	}

}
