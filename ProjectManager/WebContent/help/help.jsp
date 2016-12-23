<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>

<p> This is the Help pages</p>

<f:view>

</f:view>
</body>
<script type="text/javascript">
 
    $(document).ready(function () {
 
        $('#StudentTableContainer').jtable({
            title: 'Student List',
            paging: true, //Enable paging
            sorting: true, //Enable sorting
            defaultSorting: 'Name ASC',
            //openChildAsAccordion: true, //Enable this line to show child tabes as accordion style
            actions: {
                listAction: '/Demo/StudentList',
                deleteAction: '/Demo/DeleteStudent',
                updateAction: '/Demo/UpdateStudent',
                createAction: '/Demo/CreateStudent")'
            },
            fields: {
                StudentId: {
                    key: true,
                    create: false,
                    edit: false,
                    list: false
                },
                //CHILD TABLE DEFINITION FOR "PHONE NUMBERS"
                Phones: {
                    title: '',
                    width: '5%',
                    sorting: false,
                    edit: false,
                    create: false,
                    display: function (studentData) {
                        //Create an image that will be used to open child table
                        var $img = $('<img src="/Content/images/Misc/phone.png" title="Edit phone numbers" />');
                        //Open child table when user clicks the image
                        $img.click(function () {
                            $('#StudentTableContainer').jtable('openChildTable', $img.closest('tr'),
                                    {
                                        title: studentData.record.Name + ' - Phone numbers',
                                        actions: {
                                            listAction: '/Demo/PhoneList?StudentId=' + studentData.record.StudentId,
                                            deleteAction: '/Demo/DeletePhone',
                                            updateAction: '/Demo/UpdatePhone',
                                            createAction: '/Demo/CreatePhone'
                                        },
                                        fields: {
                                            StudentId: {
                                                type: 'hidden',
                                                defaultValue: studentData.record.StudentId
                                            },
                                            PhoneId: {
                                                key: true,
                                                create: false,
                                                edit: false,
                                                list: false
                                            },
                                            PhoneType: {
                                                title: 'Phone type',
                                                width: '30%',
                                                options: { '1': 'Home phone', '2': 'Office phone', '3': 'Cell phone' }
                                            }
                                        }
                                    }, function (data) { //opened handler
                                        data.childTable.jtable('load');
                                    });
                        });
                        //Return image to show on the person row
                        return $img;
                    }
                },
                //CHILD TABLE DEFINITION FOR "EXAMS"
                Exams: {
                    title: '',
                    width: '5%',
                    sorting: false,
                    edit: false,
                    create: false,
                    display: function (studentData) {
                        //Create an image that will be used to open child table
                        var $img = $('<img src="/Content/images/Misc/note.png" title="Edit exam results" />');
                        //Open child table when user clicks the image
                        $img.click(function () {
                            $('#StudentTableContainer').jtable('openChildTable',
                                    $img.closest('tr'), //Parent row
                                    {
                                    title: studentData.record.Name + ' - Exam Results',
                                    actions: {
                                        listAction: '/Demo/ExamList?StudentId=' + studentData.record.StudentId,
                                        deleteAction: '/Demo/DeleteExam',
                                        updateAction: '/Demo/UpdateExam',
                                        createAction: '/Demo/CreateExam'
                                    },
                                    fields: {
                                        StudentId: {
                                            type: 'hidden',
                                            defaultValue: studentData.record.StudentId
                                        },
                                        StudentExamId: {
                                            key: true,
                                            create: false,
                                            edit: false,
                                            list: false
                                        },
                                        CourseName: {
                                            title: 'Course name',
                                            width: '40%'
                                        },
                                        ExamDate: {
                                            title: 'Exam date',
                                            width: '30%',
                                            type: 'date',
                                            displayFormat: 'yy-mm-dd'
                                        },
                                        Degree: {
                                            title: 'Degree',
                                            width: '10%',
                                            options: ["AA", "BA", "BB", "CB", "CC", "DC", "DD", "FF"]
                                        }
                                    }
                                }, function (data) { //opened handler
                                    data.childTable.jtable('load');
                                });
                        });
                        //Return image to show on the person row
                        return $img;
                    }
                }
            }
        });
 
        //Load student list from server
        $('#StudentTableContainer').jtable('load');
 
    });
 
</script>



</html>