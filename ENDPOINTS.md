Patients & /api/patients & GET, POST \\
Patients & /api/patients/\{id\} & GET, PUT \\
Doctors & /api/doctors & GET, POST \\
Doctors & /api/doctors/\{id\} & GET, PUT \\
Schedules & /api/doctors/\{doctorId\}/schedules & GET, POST \\
Availability & /api/availability/doctors/\{doctorId\} & GET \\
Appointments & /api/appointments & GET, POST \\
Appointments & /api/appointments/\{id\} & GET \\
Appointments & /api/appointments/\{id\}/confirm & PUT \\
Appointments & /api/appointments/\{id\}/cancel & PUT \\
Appointments & /api/appointments/\{id\}/complete & PUT \\
Appointments & /api/appointments/\{id\}/no-show & PUT \\
Offices & /api/offices & GET, POST \\
Offices & /api/offices/\{id\} & PUT \\
Catalogs & /api/appointment-types & GET, POST \\
Specialties & /api/specialties & GET, POST \\
Reports & /api/reports/office-occupancy & GET \\
Reports & /api/reports/doctor-productivity & GET \\
Reports & /api/reports/no-show-patients & GET \\
Reports & /api/reports/specialty-cancellations & GET \\
